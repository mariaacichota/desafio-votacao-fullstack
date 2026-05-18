import { api } from "./api";

export async function getAssociados() {
    const response = await api.get("/associados");
    return response.data;
}