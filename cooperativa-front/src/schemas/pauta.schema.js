import { z } from "zod";

export const pautaSchema = z.object({
    titulo: z
        .string()
        .min(3, "Título obrigatório"),

    descricao: z
        .string()
        .min(5, "Descrição obrigatória"),

    assuntoId: z
        .string()
        .min(1, "Selecione um assunto"),
});