export const KEY_TOKEN: string = "accessToken";
export const KEY_REFRESH_TOKEN: string = "refreshToken";

export const setToken = (token: string) => {
  localStorage.setItem(KEY_TOKEN, token);
};

export const getToken = (): string | null => {
  return localStorage.getItem(KEY_TOKEN);
};

export const removeToken = (): void => {
  localStorage.removeItem(KEY_TOKEN);
};

export const setRefreshToken = (token: string) => {
  localStorage.setItem(KEY_REFRESH_TOKEN, token);
};

export const getRefreshToken = (): string | null => {
  return localStorage.getItem(KEY_REFRESH_TOKEN);
};

export const removeRefreshToken = (): void => {
  localStorage.removeItem(KEY_REFRESH_TOKEN);
};