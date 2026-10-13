-- Create your account first, then copy its UUID from Supabase > Authentication > Users.
-- Replace the placeholder UUID and run this privately in Supabase SQL Editor.
update public.profiles set role='admin'
where id='00000000-0000-0000-0000-000000000000'::uuid;
