import { useQuery } from "@tanstack/react-query";
import { getAssociados } from "../services/associado.service";

export function useAssociados() {
    return useQuery({
        queryKey: ["associados"],
        queryFn: getAssociados,
    });
}