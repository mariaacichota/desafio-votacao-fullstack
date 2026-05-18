import { z } from "zod";

export const sessaoSchema = z.object({
    duracaoMinutos: z.coerce
        .number()
        .min(1, "Mínimo 1 minuto"),
});