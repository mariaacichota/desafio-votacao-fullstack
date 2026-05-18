import Container from "../components/layout/Container";
import Header from "../components/layout/Header";
import CreatePautaForm from "../components/forms/CreatePautaForm";
import PautaCard from "../components/cards/PautaCard";
import Loading from "../components/feedback/Loading";
import EmptyState from "../components/feedback/EmptyState";
import { usePautas } from "../hooks/usePautas";

export default function Dashboard() {

    const {
        data: pautas,
        isLoading,
        isError,
    } = usePautas();

    return (
        <Container>
            <Header />
            <div className="space-y-6">
                <CreatePautaForm />
                {isLoading && (
                    <Loading />
                )}

                {isError && (
                    <EmptyState
                        message="Erro ao carregar pautas."
                    />
                )}

                {!isLoading &&
                    !isError &&
                    pautas?.length === 0 && (
                        <EmptyState
                            message="Nenhuma pauta cadastrada."
                        />
                    )}

                <div className="space-y-6">
                    {pautas?.map((pauta) => (
                        <PautaCard
                            key={pauta.id}
                            pauta={pauta}
                        />
                    ))}
                </div>
            </div>
        </Container>
    );
}