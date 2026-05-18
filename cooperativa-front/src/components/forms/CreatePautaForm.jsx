import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
import { useQueryClient } from "@tanstack/react-query";
import { pautaSchema } from "../../schemas/pauta.schema";
import { useCreatePauta } from "../../hooks/useCreatePauta";
import SelectAssunto from "./SelectAssunto";

export default function CreatePautaForm() {

    const queryClient = useQueryClient();

    const {
        register,
        control,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(pautaSchema),
        defaultValues: {
            titulo: "",
            descricao: "",
            assuntoId: "",
        },
    });

    const {
        mutateAsync,
        isPending,
    } = useCreatePauta();

    async function onSubmit(data) {
        try {
            await mutateAsync(data);
            await queryClient.refetchQueries({
                queryKey: ["pautas"],
            });

            toast.success("Pauta criada com sucesso!");

            reset({
                titulo: "",
                descricao: "",
                assuntoId: "",
            });

        } catch (error) {
            toast.error(
                error.response?.data?.message || "Erro ao criar pauta"
            );
        }
    }

    return (

        <div className="bg-white rounded-2xl shadow-sm p-6">

            <h2 className="text-xl font-bold mb-4">
                Criar nova pauta
            </h2>

            <form
                onSubmit={handleSubmit(onSubmit)}
                className="space-y-4"
            >

                <div>

                    <input
                        type="text"
                        placeholder="Título da pauta"
                        {...register("titulo")}
                        className="w-full border rounded-lg p-3"
                    />

                    {errors.titulo && (
                        <p className="text-red-500 text-sm mt-1">
                            {errors.titulo.message}
                        </p>
                    )}

                </div>

                <div>

                    <textarea
                        placeholder="Descrição da pauta"
                        {...register("descricao")}
                        className="w-full border rounded-lg p-3"
                    />

                    {errors.descricao && (
                        <p className="text-red-500 text-sm mt-1">
                            {errors.descricao.message}
                        </p>
                    )}

                </div>

                <div>

                    <Controller
                        name="assuntoId"
                        control={control}
                        render={({ field }) => (
                            <SelectAssunto
                                value={field.value}
                                onChange={field.onChange}
                            />
                        )}
                    />

                    {errors.assuntoId && (
                        <p className="text-red-500 text-sm mt-1">
                            {errors.assuntoId.message}
                        </p>
                    )}

                </div>

                <button
                    type="submit"
                    disabled={isPending}
                    className="bg-slate-900 text-white px-5 py-3 rounded-lg hover:opacity-90 transition"
                >

                    {isPending
                        ? "Criando..."
                        : "Criar pauta"}

                </button>

            </form>

        </div>
    );
}