export default function ResultCard({ resultado }) {

    const total = resultado.votosSim + resultado.votosNao;

    function getResultStyles() {
        switch (resultado.resultado) {
            case "APROVADA":
                return "bg-green-100 text-green-700";

            case "REPROVADA":
                return "bg-red-100 text-red-700";

            case "EMPATE":
                return "bg-yellow-100 text-yellow-700";

            default:
                return "bg-slate-100 text-slate-700";
        }
    }

    return (
        <div className="bg-slate-50 rounded-xl p-4 border">
            <div className="space-y-3">
                <div className="flex justify-between">
                    <span>Sim</span>

                    <strong>
                        {resultado.votosSim}
                    </strong>
                </div>

                <div className="flex justify-between">
                    <span>Não</span>

                    <strong>
                        {resultado.votosNao}
                    </strong>
                </div>

                <div className="flex justify-between">
                    <span>Total</span>

                    <strong>{total}</strong>
                </div>
            </div>

            <div
                className={`mt-5 rounded-lg p-3 text-center font-bold ${getResultStyles()}`}
            >
                {resultado.resultado}
            </div>
        </div>
    );
}