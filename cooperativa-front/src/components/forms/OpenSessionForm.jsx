import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { sessaoSchema } from "../../schemas/sessao.schema";
import { useOpenSession } from "../../hooks/useOpenSession";
import { toast } from "react-toastify";
import { useQueryClient } from "@tanstack/react-query";

export default function OpenSessionForm({ pautaId, disabled }) {

    const queryClient = useQueryClient();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(sessaoSchema),
        defaultValues: {
            duracaoMinutos: 1,
        },
    });

    const {
        mutateAsync,
        isPending,
    } = useOpenSession();

    async function onSubmit(data) {
        try {
            await mutateAsync({
                pautaId,
                data,
            });

            await queryClient.refetchQueries({
                queryKey: ["pautas"],
            });

            toast.success("Sessão aberta!");
        } catch (error) {
            toast.error(
                error.response?.data?.message ||
                "Erro ao abrir sessão"
            );
        }
    }

    return (
        <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-3"
        >
            <div>
                <input
                    type="number"
                    min="1"
                    {...register(
                        "duracaoMinutos"
                    )}
                    className="w-full border rounded-lg p-3"
                    placeholder="Duração em minutos"
                />

                {errors.duracaoMinutos && (
                    <p className="text-red-500 text-sm mt-1">
                        {
                            errors
                                .duracaoMinutos
                                .message
                        }
                    </p>
                )}
            </div>

            <button
                disabled={
                    isPending ||
                    disabled
                }
                className={`text-white px-4 py-2 rounded-lg w-full ${
                    disabled
                        ? "bg-slate-400 cursor-not-allowed"
                        : "bg-blue-600"
                }`}
            >
                {isPending
                    ? "Abrindo..."
                    : "Abrir sessão"}
            </button>
        </form>
    );
}