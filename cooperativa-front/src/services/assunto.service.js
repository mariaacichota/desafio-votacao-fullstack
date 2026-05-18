import { api } from "./api";

export async function getAssuntos() {
    const response = await api.get("/assuntos");
    return response.data;
}