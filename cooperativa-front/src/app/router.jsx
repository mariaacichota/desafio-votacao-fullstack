import { createBrowserRouter } from "react-router-dom";
import Dashboard from "../pages/Dashboard";
import PautaDetails from "../pages/PautaDetails";

export const router =
    createBrowserRouter([
        {
            path: "/",
            element: <Dashboard />,
        },
        {
            path: "/pautas/:pautaId",
            element: <PautaDetails />,
        },
    ]);