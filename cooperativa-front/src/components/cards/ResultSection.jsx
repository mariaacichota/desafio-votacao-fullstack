import { useResultado } from "../../hooks/useResultado";

import Loading from "../feedback/Loading";
import ResultCard from "./ResultCard";

export default function ResultSection({ sessaoId }) {

    const {
        data,
        isLoading,
        isError,
    } = useResultado(sessaoId);

    if (isLoading) {
        return <Loading />;
    }

    if (isError) {
        return (
            <p className="text-red-500">
                Erro ao carregar resultado
            </p>
        );
    }

    if (!data) {
        return (
            <p className="text-slate-500">
                Nenhum resultado disponível
            </p>
        );
    }

    return (
        <ResultCard
            resultado={data}
        />
    );
}