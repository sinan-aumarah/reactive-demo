import logo from './logo.png';
import './App.css';
import {useEffect, useState} from "react";
import ReactNotification, {store} from 'react-notifications-component';

import 'react-notifications-component/dist/theme.css';

function App() {
    const NOTIFICATION_SSE_URL = "/sse/v1/notifications";
    const [listening, setListening] = useState(false);

    const renderNotification = (data) => {
        store.addNotification({
            title: data.title,
            message: data.message,
            type: "success",
            insert: "top",
            container: "top-center",
            animationIn: ["animate__animated", "animate__fadeIn"],
            animationOut: ["animate__animated", "animate__fadeOut"],
            dismiss: {
                duration: 5000,
                onScreen: true
            }
        });
    }

    useEffect(() => {
        if (!listening) {
            const eventSource = new EventSource(NOTIFICATION_SSE_URL);
            eventSource.onopen = () => setListening(true);
            eventSource.onmessage = (event) => {
                const data = JSON.parse(event.data);
                renderNotification(data);
            }
            eventSource.onerror = (err) => {
                console.error("EventSource failed:", err);
                eventSource.close();
            }
        }
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>

                <ReactNotification />
                <p>
                    {listening? "SSE connection established!": "Establishing SSE connection..."}
                </p>

            </header>
        </div>
    );
}

export default App;
