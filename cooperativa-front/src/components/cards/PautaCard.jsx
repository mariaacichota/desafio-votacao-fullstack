import { Link } from "react-router-dom";
import OpenSessionForm from "../forms/OpenSessionForm";
import VoteForm from "../forms/VoteForm";
import ResultSection from "./ResultSection";
import SessionCountdown from "../session/SessionCountdown";
import SessionHistory from "../session/SessionHistory";
import SessionStatusBadge from "../session/SessionStatusBadge";
import { getActiveSession, getClosedSessions } from "../../utils/session.utils";

export default function PautaCard({ pauta }) {

    const activeSession = getActiveSession(pauta.sessoes);
    const closedSessions = getClosedSessions(pauta.sessoes);
    const latestClosedSession = closedSessions?.[0];
    const canVote = !!activeSession;
    const canOpenSession = !activeSession;

    return (
        <div className="bg-white rounded-3xl shadow-sm border p-6 hover:shadow-md transition">
            <div className="mb-8">
                <div className="flex items-start justify-between gap-6">
                    <div className="flex-1">
                        <Link to={`/pautas/${pauta.id}`}>
                            <h2 className="text-2xl font-bold text-slate-800 hover:text-blue-600 transition">
                                {pauta.titulo}
                            </h2>
                        </Link>

                        <p className="text-slate-600 mt-3">
                            {pauta.descricao}
                        </p>

                        <div className="mt-4 space-y-1">
                            <p className="text-slate-500 text-sm">
                                Assunto:{" "}
                                <strong>
                                    {
                                        pauta
                                            .assunto
                                            ?.nome
                                    }
                                </strong>
                            </p>

                            <p className="text-slate-500 text-sm">
                                Criada em:{" "}
                                {new Date(
                                    pauta.criadaEm
                                ).toLocaleDateString(
                                    "pt-BR"
                                )}
                            </p>
                        </div>
                    </div>

                    <div>
                        {activeSession ? (
                            <div className="space-y-2">
                                <SessionStatusBadge
                                    aberta={
                                        true
                                    }
                                />

                                <SessionCountdown
                                    sessao={
                                        activeSession
                                    }
                                />
                            </div>
                        ) : (
                            <SessionStatusBadge
                                aberta={false}
                            />
                        )}
                    </div>
                </div>
            </div>

            <div className="grid gap-6 lg:grid-cols-3">
                <section className="bg-slate-50 rounded-2xl p-5">
                    <h3 className="font-bold mb-4">
                        Sessão
                    </h3>

                    <OpenSessionForm
                        pautaId={
                            pauta.id
                        }
                        disabled={
                            !canOpenSession
                        }
                    />
                </section>

                <section className="bg-slate-50 rounded-2xl p-5">
                    <h3 className="font-bold mb-4">
                        Registrar voto
                    </h3>

                    <VoteForm
                        sessaoId={
                            activeSession?.id
                        }
                        assuntoId={
                            pauta.assunto
                                ?.id
                        }
                        disabled={
                            !canVote
                        }
                    />
                </section>

                <section className="bg-slate-50 rounded-2xl p-5">
                    <h3 className="font-bold mb-4">
                        Resultado
                    </h3>

                    {activeSession ? (
                        <div className="text-slate-500 text-sm">
                            Resultado disponível
                            após o encerramento
                            da sessão.
                        </div>
                    ) : latestClosedSession ? (
                        <ResultSection
                            sessaoId={
                                latestClosedSession.id
                            }
                        />
                    ) : (
                        <div className="text-slate-400 text-sm">
                            Nenhuma sessão
                            finalizada.
                        </div>
                    )}
                </section>
            </div>

            <SessionHistory
                sessoes={closedSessions}
            />
        </div>
    );
}