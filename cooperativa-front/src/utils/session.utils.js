export function isSessionClosed(sessao) {
    return !sessao.aberta;
}

export function getRemainingTime(sessao) {

    const diff = new Date(sessao.fim) - new Date();
    return Math.max(diff, 0);
}

export function getActiveSession(sessoes = []) {
    return (
        sessoes.find(
            (sessao) => sessao.aberta
        ) || null
    );
}

export function getClosedSessions(sessoes = []) {
    return sessoes.filter(
        (sessao) => !sessao.aberta
    );
}