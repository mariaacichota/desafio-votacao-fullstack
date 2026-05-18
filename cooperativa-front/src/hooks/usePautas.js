import { useQuery } from "@tanstack/react-query";
import { getPautas } from "../services/pauta.service";

export function usePautas() {
    return useQuery({
        queryKey: ["pautas"],

        queryFn: getPautas,
    });
}