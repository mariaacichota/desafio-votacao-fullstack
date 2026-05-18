import { useAssuntos } from "../../hooks/useAssuntos";

export default function SelectAssunto({ value, onChange }) {

    const {
        data: assuntos,
        isLoading,
    } = useAssuntos();

    return (
        <select
            value={value || ""}
            onChange={(e) =>
                onChange(e.target.value)
            }
            className="w-full border rounded-lg p-3"
        >
            <option value="">
                {isLoading
                    ? "Carregando assuntos..."
                    : "Selecione um assunto"}
            </option>

            {assuntos?.map((assunto) => (
                <option
                    key={assunto.id}
                    value={assunto.id}
                >
                    {assunto.nome}
                </option>
            ))}
        </select>
    );
}