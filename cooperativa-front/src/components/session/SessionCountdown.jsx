import { useEffect, useState } from "react";
import { getRemainingTime } from "../../utils/session.utils";

export default function SessionCountdown({ sessao }) {

    const [time, setTime] = useState(
        getRemainingTime(sessao)
    );

    useEffect(() => {
        const interval = setInterval(() => {
            setTime(
                getRemainingTime(sessao)
            );
        }, 1000);

        return () =>
            clearInterval(interval);
    }, [sessao]);

    const minutes = Math.floor(
        time / 1000 / 60
    );

    const seconds = Math.floor(
        (time / 1000) % 60
    );

    return (
        <div className="text-blue-600 font-medium text-sm">
            Encerra em{" "}
            {String(minutes).padStart(
                2,
                "0"
            )}
            :
            {String(seconds).padStart(
                2,
                "0"
            )}
        </div>
    );
}