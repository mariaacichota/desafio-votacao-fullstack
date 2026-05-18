import { useQuery } from "@tanstack/react-query";
import { getResultado } from "../services/pauta.service";

export function useResultado( sessaoId ) {
    return useQuery({
        queryKey: [
            "resultado",
            sessaoId,
        ],

        queryFn: () =>
            getResultado(sessaoId),

        enabled: !!sessaoId,

        refetchInterval: 5000,
    });
}