1️⃣ Frontend calls /api/auth/{provider}/login-url
2️⃣ Backend finds the provider:

OAuth2Provider provider = factory.getProvider(ServiceProvider.GOOGLE);
String url = provider.buildAuthRequestUrl(params);

3️⃣ Frontend redirects user.

4️⃣ After redirect back, frontend POSTs code to:
/api/auth/{provider}/callback

5️⃣ You call: provider.registerAccount(code, uniqueId, otherParams);

✅ Unified design → pluggable providers → easy to add new ones later.

-> GOOGLE: 
1. Go to google console and create an application first
2. Configure the application
3. Create an OAuth2 client id 
   -> select web app (or mobile whatever platform)
   -> Give application name (used) 
   -> Authorized JavaScript origins : http://localhost:4200 for example, 
      this allows the frontend to open the google oauth window from this url
   -> Authorized redirect URIs: http://localhost:4200/oauth2/google/callback for example,
      this is the url where google redirects the users wih a code as query-param upon successful auth in google.
4. After all the above steps successful, frontend receives the code, so it will send the code now to 
   backend and it will get a token back from google.

client id     : 66513506352-7rn04m5sd7alpq7vf8jn99p8v3fsg5ka.apps.googleusercontent.com
client secret : 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

linkedin 
client id     : 86v14sk149kop0
client secret : WPL_AP1.o2xrq8KxUm1j0V7V.ybYEaQ==


   
