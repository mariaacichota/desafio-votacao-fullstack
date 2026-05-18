import { useQuery } from "@tanstack/react-query";
import { getAssuntos } from "../services/assunto.service";

export function useAssuntos() {
    return useQuery({
        queryKey: ["assuntos"],
        queryFn: getAssuntos,
    });
}