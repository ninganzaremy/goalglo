import {createContext} from "react";
import {useAuthHook} from "../hooks/useAuthHook";

export const AuthContext = createContext(null);

export const AuthProvider = ({children}) => {
   const auth = useAuthHook();
   return <AuthContext.Provider value={auth}>{children}</AuthContext.Provider>;
};