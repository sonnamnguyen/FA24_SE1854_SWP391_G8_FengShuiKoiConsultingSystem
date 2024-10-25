interface OAuthConfigType {
    clientId: string;
    redirectUri: string;
    authUri: string;
  }
  
  export const OAuthConfig: OAuthConfigType = {
    clientId: "1014016100667-asu46qq4b1qn8ms3hprim9i12kooavn0.apps.googleusercontent.com",  
    redirectUri: "http://localhost:3000/authenticate",
    authUri: "https://accounts.google.com/o/oauth2/auth",
  };