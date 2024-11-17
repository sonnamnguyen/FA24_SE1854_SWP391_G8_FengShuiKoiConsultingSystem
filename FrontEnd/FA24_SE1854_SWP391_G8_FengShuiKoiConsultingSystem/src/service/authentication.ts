import { removeToken } from "./localStorageService";

export const logOut = (): void => {
  removeToken();
};