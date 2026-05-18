import { useMutation } from "@tanstack/react-query";
import { openSession } from "../services/pauta.service";

export function useOpenSession() {
    return useMutation({
        mutationFn: ({ pautaId, data }) =>
            openSession(pautaId, data),
    });
}