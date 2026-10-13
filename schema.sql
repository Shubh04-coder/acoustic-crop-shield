-- Run this entire script in Supabase SQL Editor.
create table if not exists public.profiles (
 id uuid primary key references auth.users(id) on delete cascade,
 email text,
 role text not null default 'user' check (role in ('user','admin')),
 created_at timestamptz not null default now()
);
alter table public.profiles enable row level security;
drop policy if exists "Users read their own profile" on public.profiles;
create policy "Users read their own profile" on public.profiles for select to authenticated using ((select auth.uid()) = id);

create or replace function public.handle_new_user()
returns trigger language plpgsql security definer set search_path = '' as $$
begin
 insert into public.profiles (id,email,role) values (new.id,new.email,'user')
 on conflict (id) do update set email = excluded.email;
 return new;
end; $$;
drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created after insert on auth.users for each row execute procedure public.handle_new_user();

create table if not exists public.enquiries (
 id uuid primary key default gen_random_uuid(),
 name text not null check (char_length(name) between 1 and 100),
 email text not null check (char_length(email) between 3 and 254),
 interest text not null default 'Other enquiry',
 message text not null check (char_length(message) between 1 and 3000),
 status text not null default 'new' check (status in ('new','in_progress','closed')),
 created_at timestamptz not null default now()
);
alter table public.enquiries enable row level security;
drop policy if exists "Anyone can submit an enquiry" on public.enquiries;
create policy "Anyone can submit an enquiry" on public.enquiries for insert to anon, authenticated with check (char_length(trim(name)) > 0 and char_length(trim(email)) >= 3 and char_length(trim(message)) > 0);
drop policy if exists "Admins can read enquiries" on public.enquiries;
create policy "Admins can read enquiries" on public.enquiries for select to authenticated using (exists (select 1 from public.profiles p where p.id = (select auth.uid()) and p.role = 'admin'));
drop policy if exists "Admins can update enquiries" on public.enquiries;
create policy "Admins can update enquiries" on public.enquiries for update to authenticated using (exists (select 1 from public.profiles p where p.id = (select auth.uid()) and p.role = 'admin')) with check (exists (select 1 from public.profiles p where p.id = (select auth.uid()) and p.role = 'admin'));

insert into storage.buckets (id,name,public,file_size_limit,allowed_mime_types)
values ('private-project-files','private-project-files',false,8388608,array['image/jpeg','image/png','image/webp','application/pdf'])
on conflict (id) do update set public=false,file_size_limit=8388608,allowed_mime_types=array['image/jpeg','image/png','image/webp','application/pdf'];
drop policy if exists "Users upload own project files" on storage.objects;
create policy "Users upload own project files" on storage.objects for insert to authenticated with check (bucket_id='private-project-files' and (storage.foldername(name))[1]=(select auth.uid())::text);
drop policy if exists "Users read own project files" on storage.objects;
create policy "Users read own project files" on storage.objects for select to authenticated using (bucket_id='private-project-files' and (storage.foldername(name))[1]=(select auth.uid())::text);
drop policy if exists "Users delete own project files" on storage.objects;
create policy "Users delete own project files" on storage.objects for delete to authenticated using (bucket_id='private-project-files' and (storage.foldername(name))[1]=(select auth.uid())::text);
