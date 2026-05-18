import { z } from "zod";

export const votoSchema = z.object({
    associadoId: z
        .string()
        .min(1, "Selecione um associado"),

    opcao: z.enum(["SIM", "NAO"], {
        message: "Selecione uma opção de voto",
    }),
});