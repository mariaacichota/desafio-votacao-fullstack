import { useMutation } from "@tanstack/react-query";
import { vote } from "../services/pauta.service";

export function useVote() {
    return useMutation({
        mutationFn: ({
                         sessaoId,
                         data,
                     }) =>
            vote(
                sessaoId,
                data
            ),
    });
}