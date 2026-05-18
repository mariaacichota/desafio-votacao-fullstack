import { useMutation } from "@tanstack/react-query";
import { createPauta } from "../services/pauta.service";

export function useCreatePauta() {
    return useMutation({
        mutationFn: createPauta,
    });
}