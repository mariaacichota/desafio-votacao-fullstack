import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { votoSchema } from "../../schemas/voto.schema";
import { useVote } from "../../hooks/useVote";
import SelectAssociado from "./SelectAssociado";
import { toast } from "react-toastify";
import { useQueryClient } from "@tanstack/react-query";
import { getApiErrorMessage } from "../../utils/api-error.utils";

export default function VoteForm({ sessaoId, disabled }) {

    const queryClient = useQueryClient();

    const {
        register,
        control,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(votoSchema),
        defaultValues: {
            associadoId: "",
            opcao: "SIM",
        },
    });

    const {
        mutateAsync,
        isPending,
    } = useVote();

    async function onSubmit(data) {
        try {
            await mutateAsync({
                sessaoId,
                data,
            });

            await queryClient.refetchQueries({
                queryKey: ["pautas"],
            });

            await queryClient.refetchQueries({
                queryKey: ["resultado"],
            });

            toast.success("Voto computado!");

            reset({
                associadoId: "",
                opcao: "SIM",
            });
        } catch (error) {
            toast.error(
                getApiErrorMessage(
                    error,
                    "Erro ao votar"
                )
            );
        }
    }

    return (
        <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-3"
        >
            <div>
                <Controller
                    name="associadoId"
                    control={control}
                    render={({ field }) => (
                        <SelectAssociado
                            value={field.value}
                            onChange={field.onChange}
                            disabled={disabled || isPending}
                        />
                    )}
                />

                {errors.associadoId && (
                    <p className="text-red-500 text-sm mt-1">
                        {errors.associadoId.message}
                    </p>
                )}
            </div>

            <div>
                <select
                    {...register("opcao")}
                    disabled={disabled || isPending}
                    className="w-full border rounded-lg p-3"
                >
                    <option value="SIM">
                        Sim
                    </option>

                    <option value="NAO">
                        Não
                    </option>
                </select>

                {errors.opcao && (
                    <p className="text-red-500 text-sm mt-1">
                        {errors.opcao.message}
                    </p>
                )}
            </div>

            <button
                type="submit"
                disabled={isPending || disabled || !sessaoId}
                className={`text-white px-4 py-2 rounded-lg w-full ${
                    disabled || !sessaoId
                        ? "bg-slate-400 cursor-not-allowed"
                        : "bg-green-600"
                }`}
            >
                {isPending
                    ? "Enviando..."
                    : "Votar"}
            </button>
        </form>
    );
}