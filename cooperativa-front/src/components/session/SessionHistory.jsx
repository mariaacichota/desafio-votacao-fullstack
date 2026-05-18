import { useState } from "react";
import { ChevronDown } from "lucide-react";

export default function SessionHistory({ sessoes }) {

    const [open, setOpen] =
        useState(false);

    if (!sessoes?.length) {
        return null;
    }

    function getResultadoStyle(resultado) {
        switch (resultado) {
            case "APROVADA":
                return "text-green-600";

            case "REPROVADA":
                return "text-red-600";

            case "EMPATE":
                return "text-yellow-600";

            default:
                return "text-slate-500";
        }
    }

    return (
        <div className="mt-6 border rounded-2xl overflow-hidden">
            <button
                onClick={() =>
                    setOpen(!open)
                }
                className="w-full flex items-center justify-between p-4 bg-slate-50 hover:bg-slate-100 transition"
            >
                <span className="font-medium">
                    Sessões anteriores (
                    {sessoes.length})
                </span>

                <ChevronDown
                    size={18}
                    className={`transition ${
                        open
                            ? "rotate-180"
                            : ""
                    }`}
                />
            </button>

            {open && (
                <div className="overflow-x-auto">
                    <table className="w-full text-sm">
                        <thead className="bg-slate-100">
                        <tr>
                            <th className="text-left p-3">
                                Sessão
                            </th>

                            <th className="text-left p-3">
                                Data
                            </th>

                            <th className="text-left p-3">
                                Duração
                            </th>

                            <th className="text-left p-3">
                                Resultado
                            </th>
                        </tr>
                        </thead>

                        <tbody>
                        {sessoes.map(
                            (sessao) => (
                                <tr
                                    key={
                                        sessao.id
                                    }
                                    className="border-t"
                                >
                                    <td className="p-3">
                                        #
                                        {sessao.id.slice(
                                            0,
                                            8
                                        )}
                                    </td>

                                    <td className="p-3">
                                        {new Date(
                                            sessao.inicio
                                        ).toLocaleString(
                                            "pt-BR"
                                        )}
                                    </td>

                                    <td className="p-3">
                                        {
                                            sessao.duracaoEmMinutos
                                        }{" "}
                                        min
                                    </td>

                                    <td
                                        className={`p-3 font-medium ${getResultadoStyle(
                                            sessao.resultado
                                        )}`}
                                    >
                                        {sessao.resultado ||
                                            "-"}
                                    </td>
                                </tr>
                            )
                        )}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}