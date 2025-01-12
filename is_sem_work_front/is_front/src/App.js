import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import LoginForm from './auth_pages/LoginForm/LoginForm';
import RegisterForm from './auth_pages/RegistrationForm/RegisterForm';
import HomePage from './users_pages/HomePage/HomePage';
import CreateDreamPage from "./users_pages/CreationDreamPage/CreationDreamPage";
import SelectTemplatePage from "./users_pages/SelectTemplatePage/SelectTemplatePage";
import ArchitectPage from "./users_pages/ArchitectPage/ArchitectPage";
import CreationTemplatesPage from "./users_pages/CreationTemplatesPage/CreationTemplatesPage";
import SelectArchitectPage from "./users_pages/SelectArchitectPage/SelectArchitectPage";
import SelectDateTimePage from "./users_pages/SelectDateTimePage/SelectDateTimePage";
import ReservationPage from "./users_pages/ReservationPage/ReservationPage";
import ConfirmReservationPage from "./users_pages/ConfirmReservationPage/ConfirmReservationPage";

function App() {
    const [token, setToken] = useState(null);
    const [username, setUsername] = useState('');
    const storedUsername = sessionStorage.getItem('username');

    useEffect(() => {
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);

    return (
        <Router>
            <div className="App">
                <Routes>
                    {/* Authentication Routes */}
                    <Route path="/login" element={<LoginForm onLogin={setToken} />} />
                    <Route path="/register" element={<RegisterForm />} />
                    <Route path="/" element={<Navigate to="/login" />} />

                    {/* User Pages */}
                    <Route path="/dreams/home-page" element={<HomePage />} />
                    <Route path="/dreams/create-own-dream" element={<CreateDreamPage />} />
                    <Route path="/dreams/templates" element={<SelectTemplatePage />} />

                    {/* Architect Pages */}
                    <Route path="/dreams/architect-page" element={<ArchitectPage />} />
                    <Route path="/dreams/architect-page/create-template" element={<CreationTemplatesPage />} />

                    {/* Dream Workflow Routes with dreamId */}
                    <Route path="/dreams/select-architect/:dreamId" element={<SelectArchitectPage />} />
                    <Route path="/dreams/select-datetime/:dreamId" element={<SelectDateTimePage />} />
                    <Route path="/dreams/confirm-selection/:dreamId" element={<ReservationPage />} />
                    <Route path="/dreams/success" element={<ConfirmReservationPage />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
