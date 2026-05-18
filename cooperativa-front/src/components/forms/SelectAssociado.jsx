import { useAssociados } from "../../hooks/useAssociados";

export default function SelectAssociado({ value, onChange, disabled }) {

    const {
        data: associados,
        isLoading,
        isError,
    } = useAssociados();

    return (
        <div>
            <select
                value={value || ""}
                onChange={(event) =>
                    onChange(event.target.value)
                }
                disabled={disabled || isLoading}
                className="w-full border rounded-lg p-3"
            >
                <option value="">
                    {isLoading
                        ? "Carregando associados..."
                        : "Selecione um associado"}
                </option>

                {associados?.map((associado) => (
                    <option
                        key={associado.id}
                        value={associado.id}
                    >
                        {associado.nome}
                    </option>
                ))}
            </select>

            {isError && (
                <p className="text-red-500 text-sm mt-1">
                    Erro ao carregar associados
                </p>
            )}
        </div>
    );
}