import React, { useEffect, useState } from 'react';
import {
    fetchImportHistory,
    handleDownloadLink,
    importCSVFile,
    uploadFileToMinio
} from '../../services/api';
import "./ImportCSV.css";

const ImportCSV = ({ token, importHistory, setImportHistory }) => {
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);

    const formatDateTime = (dateString) => {
        if (!dateString) return " ";
        const date = new Date(dateString);
        return date.toLocaleDateString('ru-RU', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
        }) + ` ${date.toLocaleTimeString('ru-RU')}`;
    };

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleImport = async () => {
        if (!file) {
            setError("Выберите файл для импорта.");
            return;
        }

        setLoading(true);
        setSuccessMessage(null);
        setError(null);
        try {
            const newHistoryEntry = await importCSVFile(file, token);
            setError(null);
            setSuccessMessage("Файл успешно импортирован.");
            setImportHistory((prevHistory) => [newHistoryEntry, ...prevHistory]);
            fetchImportHistoryData();
        } catch (error) {
            setSuccessMessage(null);
            setError(`Ошибка импорта файла: ${error.message}`);
        } finally {
            setLoading(false);
        }
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

    const handleUploadToMinio = async () => {
        if (!file) {
            setError("Выберите файл для загрузки.");
            return;
        }

        setLoading(true);
        setSuccessMessage(null);
        setError(null);

        try {
            const result = await uploadFileToMinio(file, token);
            setSuccessMessage(`Файл загружен в MinIO: ${result.objectName}`);

            const newEntry = {
                id: Date.now(),
                creatorName: "user",
                status: "Uploaded to MinIO",
                addedObjects: 0,
                startTime: new Date().toISOString(),
                fileName: result.objectName,
            };
            setImportHistory((prevHistory) => [newEntry, ...prevHistory]);
        } catch (error) {
            setError(`Ошибка загрузки файла: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };



    useEffect(() => {
        const intervalId = setInterval(() => {
            fetchImportHistoryData();
        }, 40000);

        return () => clearInterval(intervalId);
    }, [token]);

    return (
        <div className="import-csv-container">
            <h2>Импорт объектов из файла</h2>
            {error && <p className="error-message">{error}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}

            <div className="file-input-container">
                <input
                    type="file"
                    accept=".csv"
                    onChange={handleFileChange}
                    id="file-input"
                    disabled={loading}
                />
                <label htmlFor="file-input" className="file-label">
                    {file ? file.name : "Выберите файл для загрузки"}
                </label>
            </div>

            <button onClick={handleImport} disabled={loading || !file}>
                {loading ? "Импортируется..." : "Импортировать"}
            </button>
            <button onClick={handleUploadToMinio} disabled={loading || !file}>
                {loading ? "Загружается..." : "Загрузить в MinIO"}
            </button>

            <h3>История импорта</h3>
            {importHistory.length > 0 ? (
                <div className="history-table-container">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Creator</th>
                            <th>Status</th>
                            <th>Added Objects</th>
                            <th>Date of importing</th>
                            <th>MinIO File</th>
                        </tr>
                        </thead>
                        <tbody>
                        {importHistory.map((item) => (
                            <tr key={item.id}>
                                <td>{item.id}</td>
                                <td>{item.creatorName || " "}</td>
                                <td>{item.status || " "}</td>

                                <td>{item.addedObjects || " "}</td>
                                <td>{formatDateTime(item.startTime)}</td>
                                <td>
                                    {item.fileName ? (
                                        <button onClick={() => handleDownloadLink(item.fileName)}>
                                            Скачать
                                        </button>
                                    ) : "Нет файла"}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

            ) : (
                <p>История импорта пуста.</p>
            )}
        </div>

    );
};

export default ImportCSV;
