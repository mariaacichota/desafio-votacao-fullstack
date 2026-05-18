export default function EmptyState({message}) {
    return (
        <div className="bg-white border rounded-2xl p-10 text-center shadow-sm">
            <p className="text-slate-500 text-lg">
                {message}
            </p>
        </div>
    );
}