import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../services/api";
import Container from "../components/layout/Container";
import SessionStatusBadge from "../components/session/SessionStatusBadge";
import VoteForm from "../components/forms/VoteForm";
import OpenSessionForm from "../components/forms/OpenSessionForm";

export default function PautaDetails() {

    const { pautaId } = useParams();
    const [pauta, setPauta] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        carregarPauta();
    }, [pautaId]);

    async function carregarPauta() {
        try {
            setLoading(true);

            const { data } =
                await api.get(
                    `/pautas/${pautaId}`
                );

            setPauta(data);
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return (
            <Container>
                <p>
                    Carregando pauta...
                </p>
            </Container>
        );
    }

    if (!pauta) {
        return (
            <Container>
                <p>
                    Pauta não encontrada.
                </p>
            </Container>
        );
    }

    return (
        <Container>
            <div className="flex flex-col gap-6">
                <div className="bg-white rounded-2xl shadow p-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-2xl font-bold">
                                {pauta.titulo}
                            </h1>
                            <p className="text-gray-600 mt-2">
                                {pauta.descricao}
                            </p>
                        </div>
                        <div className="bg-slate-100 px-4 py-2 rounded-xl text-sm font-medium">
                            {pauta.assunto?.nome}
                        </div>
                    </div>
                </div>

                <OpenSessionForm
                    pautaId={pauta.id}
                    onSuccess={carregarPauta}
                />

                <div className="flex flex-col gap-4">
                    {pauta.sessoes?.map(
                        (sessao) => (
                            <div
                                key={sessao.id}
                                className="bg-white rounded-2xl shadow p-6 flex flex-col gap-4"
                            >
                                <div className="flex items-center justify-between">
                                    <h2 className="font-semibold">
                                        Sessão
                                    </h2>
                                    <SessionStatusBadge
                                        aberta={sessao.aberta}
                                        resultado={sessao.resultado}
                                    />
                                </div>
                                <div className="text-sm text-gray-600">
                                    <p>
                                        Início:{" "}
                                        {new Date(
                                            sessao.inicio
                                        ).toLocaleString(
                                            "pt-BR"
                                        )}
                                    </p>
                                    <p>
                                        Fim:{" "}
                                        {new Date(
                                            sessao.fim
                                        ).toLocaleString(
                                            "pt-BR"
                                        )}
                                    </p>
                                    <p>
                                        Duração:{" "}
                                        {
                                            sessao.duracaoEmMinutos
                                        } min
                                    </p>
                                </div>

                                {sessao.aberta ? (
                                    <VoteForm
                                        sessaoId={sessao.id}
                                        onSuccess={carregarPauta}
                                    />

                                ) : (
                                    <div className="text-sm text-slate-500">
                                        Sessão encerrada.
                                    </div>
                                )}
                            </div>
                        )
                    )}
                </div>
            </div>
        </Container>
    );
}