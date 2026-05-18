import { api } from "./api";

export async function createPauta(data) {
    const response =
        await api.post(
            "/pautas",
            data
        );

    return response.data;
}

export async function openSession( pautaId, data) {

    const response =
        await api.post(
            `/pautas/${pautaId}/sessoes`,
            data
        );

    return response.data;
}

export async function vote(sessaoId, data) {

    const response =
        await api.post(
            `/sessoes/${sessaoId}/votos`,
            data
        );

    return response.data;
}

export async function getResultado(sessaoId) {

    const response =
        await api.get(
            `/sessoes/${sessaoId}/resultado`
        );

    return response.data;
}

export async function getPautas() {

    const response =
        await api.get("/pautas");

    return response.data;
}