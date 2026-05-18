export default function SessionStatusBadge({ aberta, resultado }) {

    if (aberta) {
        return (
            <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm">
        Aberta
      </span>
        );
    }

    if (resultado === "APROVADA") {
        return (
            <span className="bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-sm">
        Aprovada
      </span>
        );
    }

    if (resultado === "REPROVADA") {
        return (
            <span className="bg-red-100 text-red-700 px-3 py-1 rounded-full text-sm">
        Reprovada
      </span>
        );
    }

    if (resultado === "EMPATE") {
        return (
            <span className="bg-yellow-100 text-yellow-700 px-3 py-1 rounded-full text-sm">
        Empate
      </span>
        );
    }

    return (
        <span className="bg-gray-100 text-gray-700 px-3 py-1 rounded-full text-sm">
      Encerrada
    </span>
    );
}