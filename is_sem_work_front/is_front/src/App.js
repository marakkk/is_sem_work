import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import BookCreatureForm from './components/BookCreatureForm/BookCreatureForm';
import LoginForm from './components/LoginForm/LoginForm';
import RegisterForm from './components/RegistrationForm/RegisterForm';
import UserApprovalList from './components/UserApprovalList/UserApprovalList';
import {
    deleteBookCreature, updateBookCreature, createBookCreature, getBookCreatures, fetchImportHistory
} from './services/api';
import Content from './components/Content/Content';
import SimpleRow from './components/SimpleRow/SimpleRow';
import BookCreatureList from "./components/BookCreatureList/BookCreatureList";
import Button from "./components/Button/Button";
import SpecialActions from "./components/SpecialActions/SpecialActions";
import ImportCSV from './components/ImportCSV/ImportCSV';

function App() {
    const [bookCreatures, setBookCreatures] = useState([]);
    const [selectedCreatures, setSelectedCreatures] = useState([]);
    const [token, setToken] = useState(null);
    const [activePanel, setActivePanel] = useState('create');
    const [selectedCreature, setSelectedCreature] = useState(null);
    const [importHistory, setImportHistory] = useState([]);
    const [username, setUsername] = useState('');
    const isApproved = sessionStorage.getItem('isApproved') === 'true';
    const storedUsername = sessionStorage.getItem('username');

    useEffect(() => {
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);

    const fetchUpdatedCreatures = async () => {
        const data = await getBookCreatures(token);
        setBookCreatures(data);
    };

    useEffect(() => {
        if (token) {
            fetchUpdatedCreatures();
        }
    }, [token]);

    const handleCreate = async (creature) => {
        await createBookCreature(creature, token);
        fetchUpdatedCreatures();
    };

    const handleUpdate = async (id, updatedCreature) => {
        await updateBookCreature(id, updatedCreature, token);
        fetchUpdatedCreatures();
    };

    const handleDelete = async (id) => {
        await deleteBookCreature(id, token);
        fetchUpdatedCreatures();
    };

    const PrivateRoute = ({ children }) => {
        return token ? children : <Navigate to="/login" />;
    };

    const handleSelectCreatureForUpdate = (creature) => {
        setSelectedCreature(creature);
        setActivePanel('update');
    };

    const handleChangeUser = () => {
        setToken(null);
        sessionStorage.clear();
    };

    const fetchImportHistoryData = async () => {
        if (token) {
            try {
                const history = await fetchImportHistory(token);
                setImportHistory(history);
            } catch (error) {
                console.error("Ошибка получения истории импортов:", error);
            }
        }
    };

    const renderUserApprovalButton = () => {
        if (isApproved) {
            return <Button onClick={() => setActivePanel('approval')} label="Заявки на одобрение" />;
        }
        return null;
    };

    const renderImportButton = () => {
        return (
            <Button onClick={() => { setActivePanel('import'); fetchImportHistoryData(); }} label="Импорт данных" />
        );
    };

    return (
        <Router>
            <div className="App">
                {token && (
                    <header className="welcome-header">
                        <h2>Добро пожаловать, {storedUsername}!</h2>
                    </header>
                )}
                <Routes>
                    <Route
                        path="/dreams"
                        element={
                            <PrivateRoute>
                                <Content className="content--fullscreen-height">
                                    <SimpleRow className="button-container">
                                        <Button onClick={() => setActivePanel('create')} label="Создать сущность" />
                                        <Button onClick={() => setActivePanel('update')} label="Список сущностей" />
                                        {renderUserApprovalButton()}
                                        {renderImportButton()}
                                        <Button onClick={handleChangeUser} label="Сменить пользователя" />
                                    </SimpleRow>

                                    {activePanel === 'create' ? (
                                        <BookCreatureForm onCreate={handleCreate} />
                                    ) : activePanel === 'update' ? (
                                        <BookCreatureList
                                            creatures={bookCreatures}
                                            onUpdate={handleUpdate}
                                            onDelete={handleDelete}
                                            setSelectedCreatures={setSelectedCreatures}
                                            onSelect={handleSelectCreatureForUpdate}
                                            fetchUpdatedCreatures={fetchUpdatedCreatures}
                                        />
                                    ) :  (
                                        <UserApprovalList token={token} />
                                    )}

                                </Content>
                            </PrivateRoute>
                        }
                    />
                    <Route path="/login" element={<LoginForm onLogin={setToken} />} />
                    <Route path="/register" element={<RegisterForm />} />
                    <Route path="/" element={<Navigate to="/login" />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
