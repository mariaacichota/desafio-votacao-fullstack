export function getApiErrorMessage(error, fallback = "Erro inesperado") {
    const data = error?.response?.data;

    if (!data) {
        return fallback;
    }

    if (typeof data === "string") {
        return data;
    }

    return (
        data.message ||
        data.mensagem ||
        data.error ||
        data.erro ||
        data.detail ||
        fallback
    );
}