# Acoustic Crop Shield website

Responsive React + Vite website for your Acoustic Crop Shield prototype concept. Includes a product landing page, enquiry form, Supabase email/password authentication, private per-user project uploads, and a role-gated enquiry admin panel.

**Important:** The website code is prepared, but it is not yet live on a public URL. The backend becomes active only after you connect your own Supabase project and deploy. Hardware telemetry, live chat, notifications, and payments are not connected because the actual controller and provider accounts are not specified.

## 1. Run on your computer
Install Node.js (current LTS), unzip the project, open a terminal in the project folder, then run:

```bash
npm install
npm run dev
```

Open the local address printed by Vite, usually `http://localhost:5173`.

## 2. Connect Supabase backend
1. Create a Supabase project.
2. Open Supabase **SQL Editor** and run all of `supabase/schema.sql`.
3. Copy `.env.example` to `.env` in the project root.
4. Fill `VITE_SUPABASE_URL` and `VITE_SUPABASE_PUBLISHABLE_KEY` from Supabase Project Settings > API.
5. Restart the dev server after editing `.env`.
6. Configure Supabase Authentication site URL and allowed redirect URLs for local development and your deployed site.
7. Create an account on the website. New accounts receive `user` role automatically.
8. For the first admin, copy your own user UUID from Supabase Authentication > Users, replace the placeholder in `supabase/admin-bootstrap.example.sql`, and run it in SQL Editor. Never allow public users to assign themselves admin.
9. Test the enquiry form, admin panel and private uploads.

## 3. Deploy free-first on Vercel
1. Push this project to a GitHub repository. Do **not** commit `.env`.
2. Import the repository into Vercel. Choose Vite if prompted.
3. Build command: `npm run build`; output directory: `dist`.
4. Add `VITE_SUPABASE_URL` and `VITE_SUPABASE_PUBLISHABLE_KEY` in Vercel project environment variables.
5. Deploy and add the deployed URL to Supabase Auth's allowed redirect URLs/site URL.

Netlify also works with build command `npm run build`, publish directory `dist`, and the same environment variables. Free plans have usage limits and terms that can change.

## Security
- Never put a Supabase service-role key, database password, payment secret, or private API key in frontend code or a `VITE_` variable.
- The frontend publishable key is only appropriate with RLS enabled and policies reviewed.
- Review privacy, spam prevention, data retention, and local legal requirements before collecting real customer information.
- Hardware data is not displayed until the actual controller and secure telemetry integration are built and tested.
- Payment processing, live chat and notifications are not implemented in this starter.
