import { useEffect, useState } from 'react';
import {
  Activity, ArrowDown, ArrowRight, AudioLines, Check, CheckCircle2,
  ChevronDown, CircleHelp, CloudUpload, Cpu, FileText, Leaf, LockKeyhole,
  LogIn, Menu, MessageCircle, Radio, ShieldCheck, Sprout, Upload, Wifi,
  X, Zap, LogOut, Send, UserRound, LayoutDashboard
} from 'lucide-react';
import { isSupabaseConfigured, supabase } from './lib/supabase';

const features = [
  { icon: AudioLines, title: 'Acoustic deterrence', text: 'A sound-based approach designed to discourage unwanted bird and animal visits around crops.' },
  { icon: Cpu, title: 'Electronics-led design', text: 'A practical prototype concept combining electronic control with an acoustic output system.' },
  { icon: Activity, title: 'Monitoring-ready', text: 'A dashboard foundation for real device readings when a controller and telemetry link are connected.' },
  { icon: Leaf, title: 'Farmer-focused', text: 'Designed around practical crop-protection needs, with a focus on usability and responsible deployment.' },
];
const faqs = [
  ['What is Acoustic Crop Shield?', 'It is a developing crop-protection prototype concept that uses acoustic deterrence to help discourage birds and other crop-raiding animals.'],
  ['Does the website show live device data?', 'Not yet. Live readings appear only after a compatible controller, sensors (if used), and a secure communication link are connected. The dashboard currently labels device data as not connected.'],
  ['Will it protect every crop from every animal?', 'No system can promise that. Results depend on the species, crop, environment, sound design, placement, and animal habituation. Field testing is necessary.'],
  ['Can I request a demonstration or collaboration?', 'Yes. Use the enquiry form and share what you are interested in, such as a prototype demo, collaboration, or technical discussion.'],
];

function Brand({ onClick }) {
  return <a className="brand" href="#home" onClick={onClick} aria-label="Acoustic Crop Shield home"><span className="brand-mark"><AudioLines size={23}/></span><span>Acoustic<span className="brand-light">Crop Shield</span><small>SMARTER CROP PROTECTION</small></span></a>;
}

export default function App() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const [activeModal, setActiveModal] = useState('');
  const [session, setSession] = useState(null);
  const [profile, setProfile] = useState(null);
  const [authMode, setAuthMode] = useState('login');
  const [authBusy, setAuthBusy] = useState(false);
  const [authError, setAuthError] = useState('');
  const [authMessage, setAuthMessage] = useState('');
  const [form, setForm] = useState({ name: '', email: '', interest: 'Product demonstration', message: '' });
  const [sending, setSending] = useState(false);
  const [formError, setFormError] = useState('');
  const [formSuccess, setFormSuccess] = useState('');
  const [enquiries, setEnquiries] = useState([]);
  const [adminLoading, setAdminLoading] = useState(false);
  const [adminError, setAdminError] = useState('');
  const [uploadStatus, setUploadStatus] = useState('');

  useEffect(() => {
    if (!supabase) return;
    supabase.auth.getSession().then(({ data }) => setSession(data.session));
    const { data: listener } = supabase.auth.onAuthStateChange((_event, newSession) => setSession(newSession));
    return () => listener.subscription.unsubscribe();
  }, []);

  useEffect(() => {
    let alive = true;
    async function loadProfile() {
      if (!supabase || !session?.user) { setProfile(null); return; }
      const { data } = await supabase.from('profiles').select('id,email,role').eq('id', session.user.id).maybeSingle();
      if (alive) setProfile(data || { id: session.user.id, email: session.user.email, role: 'user' });
    }
    loadProfile();
    return () => { alive = false; };
  }, [session]);

  async function handleEnquiry(e) {
    e.preventDefault(); setSending(true); setFormError(''); setFormSuccess('');
    try {
      if (!form.name.trim() || !form.email.trim() || !form.message.trim()) throw new Error('Please complete all required fields.');
      if (isSupabaseConfigured) {
        const { error } = await supabase.from('enquiries').insert({ name: form.name.trim(), email: form.email.trim(), interest: form.interest, message: form.message.trim() });
        if (error) throw error;
        setFormSuccess('Thank you — your enquiry has been submitted. We will follow up using the email you provided.');
      } else {
        setFormSuccess('Form preview is working. To securely save enquiries, connect your Supabase project using the setup guide included with this website.');
      }
      setForm({ name: '', email: '', interest: 'Product demonstration', message: '' });
    } catch (err) { setFormError(err.message || 'Could not submit your enquiry. Please try again.'); }
    finally { setSending(false); }
  }

  async function handleAuth(e) {
    e.preventDefault(); if (!supabase) { setAuthError('Connect Supabase first. See SETUP.md in the project folder.'); return; }
    setAuthBusy(true); setAuthError(''); setAuthMessage('');
    const email = e.currentTarget.email.value.trim(); const password = e.currentTarget.password.value;
    try {
      if (authMode === 'signup') {
        const { data, error } = await supabase.auth.signUp({ email, password });
        if (error) throw error;
        setAuthMessage(data.session ? 'Account created. You are now signed in.' : 'Account created. Check your email to confirm the account, then sign in.');
      } else {
        const { error } = await supabase.auth.signInWithPassword({ email, password }); if (error) throw error;
        setActiveModal('');
      }
    } catch (err) { setAuthError(err.message || 'Authentication failed.'); }
    finally { setAuthBusy(false); }
  }

  async function signOut() { if (supabase) await supabase.auth.signOut(); setSession(null); setProfile(null); setActiveModal(''); }

  async function openAdmin() {
    setActiveModal('admin'); setAdminError(''); setAdminLoading(true);
    if (!supabase || profile?.role !== 'admin') { setAdminError('An authenticated admin account is required. Set your account role to admin using the secure SQL instructions in SETUP.md.'); setAdminLoading(false); return; }
    const { data, error } = await supabase.from('enquiries').select('*').order('created_at', { ascending: false }).limit(100);
    if (error) setAdminError(error.message); else setEnquiries(data || []);
    setAdminLoading(false);
  }

  async function updateStatus(id, status) {
    const { error } = await supabase.from('enquiries').update({ status }).eq('id', id);
    if (error) setAdminError(error.message); else setEnquiries(old => old.map(item => item.id === id ? { ...item, status } : item));
  }

  async function uploadFile(e) {
    const file = e.target.files?.[0]; if (!file) return;
    setUploadStatus('');
    if (!supabase || !session?.user) { setUploadStatus('Please sign in after connecting Supabase to upload private files.'); return; }
    if (file.size > 8 * 1024 * 1024) { setUploadStatus('File is too large. Maximum size is 8 MB.'); return; }
    if (!['image/jpeg','image/png','image/webp','application/pdf'].includes(file.type)) { setUploadStatus('Allowed formats: JPG, PNG, WEBP, PDF.'); return; }
    setUploadStatus('Uploading…');
    const path = `${session.user.id}/${Date.now()}-${file.name.replace(/[^a-zA-Z0-9._-]/g, '_')}`;
    const { error } = await supabase.storage.from('private-project-files').upload(path, file, { upsert: false, contentType: file.type });
    setUploadStatus(error ? error.message : `Uploaded privately: ${file.name}`);
    e.target.value = '';
  }

  return <>
    <div className="announcement"><span className="announcement-dot"/> A developing prototype for smarter, practical crop protection <a href="#about">Explore the concept <ArrowRight size={13}/></a></div>
    <header className="site-header"><div className="container nav-wrap"><Brand onClick={() => setMobileOpen(false)}/><nav className={mobileOpen ? 'nav-links nav-open' : 'nav-links'}><a href="#about" onClick={() => setMobileOpen(false)}>About</a><a href="#how-it-works" onClick={() => setMobileOpen(false)}>How it works</a><a href="#dashboard" onClick={() => setMobileOpen(false)}>Dashboard</a><a href="#faq" onClick={() => setMobileOpen(false)}>FAQ</a><a href="#contact" onClick={() => setMobileOpen(false)}>Contact</a></nav><div className="nav-actions">{session ? <button className="button-quiet account-button" onClick={() => setActiveModal('account')}><UserRound size={16}/> Account</button> : <button className="button-quiet account-button" onClick={() => {setAuthMode('login');setActiveModal('auth');}}><LogIn size={16}/> Sign in</button>}<a className="button-primary nav-cta" href="#contact">Get in touch <ArrowRight size={16}/></a><button className="mobile-menu" aria-label="Toggle menu" onClick={() => setMobileOpen(!mobileOpen)}>{mobileOpen ? <X/> : <Menu/>}</button></div></div></header>

    <main>
      <section className="hero" id="home"><div className="hero-bg"/><div className="container hero-grid"><div className="hero-copy"><div className="eyebrow"><span className="eyebrow-line"/> FIELD-FOCUSED INNOVATION</div><h1>Give your crops a <em>smarter shield.</em></h1><p className="hero-lead">An acoustic crop-protection concept exploring how sound-based deterrence and practical electronics can help farmers respond to unwanted visitors in the field.</p><div className="hero-buttons"><a className="button-primary button-large" href="#how-it-works">Discover how it works <ArrowRight size={18}/></a><a className="button-outline button-large" href="#contact">Discuss the prototype</a></div><div className="hero-proof"><div className="proof-icon"><Sprout size={20}/></div><div><strong>Built around real farming challenges</strong><span>Prototype-led • Field testing needed</span></div></div></div><div className="hero-visual"><img className="hero-image" src="https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=1200&q=85" alt="Green agricultural fields in warm morning light"/><div className="visual-shade"/><div className="floating-note note-top"><span className="note-icon"><AudioLines size={18}/></span><span><strong>Acoustic approach</strong><small>Sound-based deterrence</small></span></div><div className="floating-note note-bottom"><span className="status-ring"><Radio size={18}/></span><span><strong>Prototype status</strong><small><i/> Development concept</small></span></div><div className="image-caption"><span>01 / THE CONCEPT</span><span>Crop protection, rethought</span></div></div></div><a className="scroll-cue" href="#about"><ArrowDown size={15}/> SCROLL TO EXPLORE</a></section>

      <section className="trust-strip"><div className="container trust-inner"><span><ShieldCheck size={18}/> Responsible-by-design messaging</span><span><Cpu size={18}/> Electronics + acoustics</span><span><Wifi size={18}/> Monitoring-ready foundation</span><span><Leaf size={18}/> Farmer-focused concept</span></div></section>

      <section className="section about-section" id="about"><div className="container"><div className="section-heading"><div><div className="eyebrow"><span className="eyebrow-line"/> THE IDEA</div><h2>A practical idea for<br/><em>a persistent problem.</em></h2></div><p>Birds and other animals can damage crops and reduce harvest quality. Acoustic Crop Shield explores a sound-based deterrence approach as one possible part of a wider crop-protection strategy.</p></div><div className="feature-grid">{features.map((item, i) => <article className="feature-card" key={item.title}><div className="feature-top"><span className="feature-icon"><item.icon size={22}/></span><span className="feature-index">0{i+1}</span></div><h3>{item.title}</h3><p>{item.text}</p><div className="feature-rule"/></article>)}</div><div className="note-banner"><span className="note-banner-icon"><CircleHelp size={20}/></span><p><strong>Honest by design:</strong> this is a developing concept, not a guarantee of crop protection. Effectiveness must be validated through safe, species-aware field testing.</p></div></div></section>

      <section className="section process-section" id="how-it-works"><div className="container"><div className="center-heading"><div className="eyebrow"><span className="eyebrow-line"/> FROM IDEA TO FIELD</div><h2>How the concept <em>comes together</em></h2><p>A simple overview of the intended system flow. Exact components and behaviour depend on the final prototype hardware.</p></div><div className="process-grid"><article className="process-step"><div className="process-number">01</div><div className="process-icon"><Activity size={25}/></div><h3>Observe the field</h3><p>Identify the crop, target animals, field conditions, and the situations where crop damage occurs.</p></article><div className="process-arrow"><ArrowRight size={20}/></div><article className="process-step"><div className="process-number">02</div><div className="process-icon"><Cpu size={25}/></div><h3>Control unit</h3><p>A controller can manage the prototype's operating logic once the selected electronics are integrated.</p></article><div className="process-arrow"><ArrowRight size={20}/></div><article className="process-step"><div className="process-number">03</div><div className="process-icon"><AudioLines size={25}/></div><h3>Acoustic output</h3><p>The system produces a planned acoustic output intended to discourage unwanted visits, subject to testing.</p></article><div className="process-arrow"><ArrowRight size={20}/></div><article className="process-step"><div className="process-number">04</div><div className="process-icon"><LayoutDashboard size={25}/></div><h3>Review and improve</h3><p>Future telemetry can help record device status and evaluate results after hardware integration.</p></article></div><div className="process-footnote"><CheckCircle2 size={17}/> No live detection, sound output, or field measurements are claimed by this website until the hardware is connected and verified.</div></div></section>

      <section className="section dashboard-section" id="dashboard"><div className="container dashboard-grid"><div className="dashboard-copy"><div className="eyebrow"><span className="eyebrow-line"/> DIGITAL CONTROL CENTRE</div><h2>A clear view of the system, <em>when it's connected.</em></h2><p>This website includes a foundation for a future monitoring dashboard. Connect your controller and a secure data service to show real device status here.</p><ul className="check-list"><li><Check size={17}/> Ready for a device-status integration</li><li><Check size={17}/> User accounts and admin access foundation</li><li><Check size={17}/> Private project-file uploads</li><li><Check size={17}/> Enquiry management for the project team</li></ul><button className="button-primary" onClick={() => session ? setActiveModal('account') : (setAuthMode('signup'),setActiveModal('auth'))}>{session ? 'Open your account' : 'Create an account'} <ArrowRight size={17}/></button></div><div className="dashboard-card"><div className="dash-head"><div><span className="dash-kicker">ACOUSTIC CROP SHIELD</span><h3>Device overview</h3></div><span className="device-status"><span/> Not connected</span></div><div className="dash-device"><div className="device-illustration"><div className="device-orbit orbit-one"/><div className="device-orbit orbit-two"/><div className="device-core"><AudioLines size={36}/></div><span className="device-dot dot-a"/><span className="device-dot dot-b"/><span className="device-dot dot-c"/></div><div className="device-label"><span>Prototype device</span><strong>Awaiting connection</strong><small>Connect hardware to enable live status</small></div></div><div className="dash-metrics"><div className="metric"><span>Device status</span><strong>—</strong><small>Not available</small></div><div className="metric"><span>Last update</span><strong>—</strong><small>No data received</small></div><div className="metric"><span>Field readings</span><strong>—</strong><small>Not configured</small></div></div><div className="dash-foot"><span><span className="live-dot"/> Live data disabled</span><span>Hardware integration required</span></div></div></div></section>

      <section className="section file-section"><div className="container file-card"><div className="file-icon"><CloudUpload size={25}/></div><div className="file-copy"><div className="eyebrow"><span className="eyebrow-line"/> PROJECT WORKSPACE</div><h2>Keep project files <em>in one place.</em></h2><p>Once Supabase is configured and you sign in, upload private project documents, prototype images, and PDF notes. Files are restricted to the signed-in account by the supplied access policies.</p><span className="file-limit"><LockKeyhole size={14}/> JPG, PNG, WEBP, PDF • 8 MB per file</span></div><div className="file-action"><label className="button-outline upload-button"><Upload size={17}/> Choose a file<input type="file" accept="image/jpeg,image/png,image/webp,application/pdf" onChange={uploadFile}/></label>{uploadStatus && <p className="upload-status" role="status">{uploadStatus}</p>}{!session && <small>Sign in to upload files</small>}</div></div></section>

      <section className="section faq-section" id="faq"><div className="container faq-layout"><div className="faq-intro"><div className="eyebrow"><span className="eyebrow-line"/> GOOD TO KNOW</div><h2>Questions, <em>answered.</em></h2><p>A few important details about the concept and this website.</p><a href="#contact" className="text-link">Still curious? Contact us <ArrowRight size={16}/></a></div><div className="faq-list">{faqs.map(([q,a],i) => <details className="faq-item" key={q} open={i===0}><summary>{q}<ChevronDown size={19}/></summary><p>{a}</p></details>)}</div></div></section>

      <section className="section contact-section" id="contact"><div className="container contact-grid"><div className="contact-copy"><div className="eyebrow"><span className="eyebrow-line"/> START A CONVERSATION</div><h2>Let's grow the idea <em>together.</em></h2><p>Interested in a prototype demonstration, a technical collaboration, or learning more about the project? Send a message.</p><div className="contact-detail"><span><MessageCircle size={19}/></span><div><strong>Project enquiries</strong><small>Share your question or collaboration idea</small></div></div><div className="contact-detail"><span><LockKeyhole size={19}/></span><div><strong>Respectful data handling</strong><small>Enquiries are stored securely when the backend is connected</small></div></div></div><div className="contact-form-card"><div className="form-heading"><div><h3>Send an enquiry</h3><p>Fields marked * are required.</p></div><span className="form-mark"><Send size={19}/></span></div><form onSubmit={handleEnquiry}><div className="form-row"><label>Full name *<input required maxLength="100" value={form.name} onChange={e=>setForm({...form,name:e.target.value})} placeholder="Your name"/></label><label>Email address *<input required type="email" maxLength="254" value={form.email} onChange={e=>setForm({...form,email:e.target.value})} placeholder="you@example.com"/></label></div><label>I'm interested in<select value={form.interest} onChange={e=>setForm({...form,interest:e.target.value})}><option>Product demonstration</option><option>Technical collaboration</option><option>Academic project</option><option>Field testing</option><option>Other enquiry</option></select></label><label>Message *<textarea required minLength="1" maxLength="3000" rows="4" value={form.message} onChange={e=>setForm({...form,message:e.target.value})} placeholder="Tell us a little about your enquiry…"/></label>{formError && <p className="form-error" role="alert">{formError}</p>}{formSuccess && <p className="form-success" role="status">{formSuccess}</p>}<button className="button-primary form-submit" type="submit" disabled={sending}>{sending?'Sending…':'Send enquiry'} <ArrowRight size={17}/></button><small className="privacy-note"><LockKeyhole size={12}/> Your information is used to respond to this enquiry.</small></form></div></div></section>
    </main>

    <footer className="site-footer"><div className="container footer-main"><div><Brand/><p className="footer-tagline">A developing acoustic crop-protection concept, built around practical thinking and responsible testing.</p></div><div className="footer-links"><strong>Explore</strong><a href="#about">About the concept</a><a href="#how-it-works">How it works</a><a href="#dashboard">Dashboard</a><a href="#contact">Contact</a></div><div className="footer-links"><strong>Workspace</strong><button onClick={()=>{setAuthMode('login');setActiveModal('auth')}}>Sign in</button><button onClick={()=>{setAuthMode('signup');setActiveModal('auth')}}>Create account</button>{profile?.role==='admin'&&<button onClick={openAdmin}>Admin panel</button>}</div><div className="footer-note"><span className="footer-leaf"><Leaf size={21}/></span><strong>Protecting what grows.</strong><p>Thoughtful technology for a changing field.</p></div></div><div className="container footer-bottom"><span>© {new Date().getFullYear()} Acoustic Crop Shield. Project concept website.</span><span>Prototype-stage project • Results require field validation</span></div></footer>

    {activeModal==='auth' && <div className="modal-backdrop" onMouseDown={e=>{if(e.target===e.currentTarget)setActiveModal('')}}><section className="modal-card" role="dialog" aria-modal="true" aria-labelledby="auth-title"><button className="modal-close" onClick={()=>setActiveModal('')} aria-label="Close"><X size={20}/></button><span className="modal-icon"><LockKeyhole size={22}/></span><h2 id="auth-title">{authMode==='login'?'Welcome back':'Create your account'}</h2><p>{authMode==='login'?'Sign in to access your project workspace.':'Create a user account for private project uploads.'}</p><form onSubmit={handleAuth}><label>Email address<input name="email" required type="email" autoComplete="email" placeholder="you@example.com"/></label><label>Password<input name="password" required minLength="8" type="password" autoComplete={authMode==='login'?'current-password':'new-password'} placeholder="At least 8 characters"/></label>{authError&&<p className="form-error" role="alert">{authError}</p>}{authMessage&&<p className="form-success" role="status">{authMessage}</p>}<button className="button-primary form-submit" disabled={authBusy}>{authBusy?'Please wait…':authMode==='login'?'Sign in':'Create account'} <ArrowRight size={17}/></button></form><p className="modal-switch">{authMode==='login'?'New to the workspace?':'Already have an account?'} <button onClick={()=>{setAuthMode(authMode==='login'?'signup':'login');setAuthError('');setAuthMessage('')}}>{authMode==='login'?'Create an account':'Sign in'}</button></p>{!isSupabaseConfigured&&<p className="setup-warning">Backend not configured yet. The website design works, but accounts and data storage require the setup steps included in the project.</p>}</section></div>}

    {activeModal==='account' && <div className="modal-backdrop" onMouseDown={e=>{if(e.target===e.currentTarget)setActiveModal('')}}><section className="modal-card account-modal" role="dialog" aria-modal="true"><button className="modal-close" onClick={()=>setActiveModal('')} aria-label="Close"><X size={20}/></button><span className="modal-icon"><UserRound size={22}/></span><h2>Your workspace</h2><p>{session?.user?.email}</p><div className="account-role"><span>Account role</span><strong>{profile?.role || 'user'}</strong></div>{profile?.role==='admin'&&<button className="button-outline full-button" onClick={openAdmin}><LayoutDashboard size={17}/> Open admin panel</button>}<label className="button-outline upload-button full-button"><Upload size={17}/> Upload project file<input type="file" accept="image/jpeg,image/png,image/webp,application/pdf" onChange={uploadFile}/></label>{uploadStatus&&<p className="upload-status">{uploadStatus}</p>}<button className="button-quiet signout-button" onClick={signOut}><LogOut size={16}/> Sign out</button></section></div>}

    {activeModal==='admin' && <div className="modal-backdrop admin-backdrop" onMouseDown={e=>{if(e.target===e.currentTarget)setActiveModal('')}}><section className="modal-card admin-modal" role="dialog" aria-modal="true"><button className="modal-close" onClick={()=>setActiveModal('')} aria-label="Close"><X size={20}/></button><span className="modal-icon"><LayoutDashboard size={22}/></span><h2>Enquiry admin</h2><p>Review submitted enquiries and update their status.</p>{adminLoading?<p>Loading enquiries…</p>:adminError?<p className="form-error">{adminError}</p>:<div className="enquiry-list">{enquiries.length===0?<p>No enquiries yet.</p>:enquiries.map(item=><article className="enquiry-item" key={item.id}><div className="enquiry-item-head"><strong>{item.name}</strong><span>{new Date(item.created_at).toLocaleString()}</span></div><a href={`mailto:${item.email}`}>{item.email}</a><p className="enquiry-interest">{item.interest}</p><p>{item.message}</p><label>Status<select value={item.status} onChange={e=>updateStatus(item.id,e.target.value)}><option value="new">New</option><option value="in_progress">In progress</option><option value="closed">Closed</option></select></label></article>)}</div>}</section></div>}
  </>;
}
