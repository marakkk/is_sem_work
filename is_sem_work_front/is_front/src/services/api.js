const API_BASE_URL = 'http://localhost:8080/api/book-creatures';

export async function getBookCreatures(token) {
  const response = await fetch(`${API_BASE_URL}/all`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });

  await handleError(response);

  const data = await response.json();
  return data;
}

export async function createBookCreature(creature, token) {
  const response = await fetch(API_BASE_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(creature),
  });

  if (!response.ok) {
    const error = await response.text();
    throw new Error(`Request failed: ${response.status} - ${error}`);
  }

  const contentType = response.headers.get('content-type');
  if (contentType && contentType.includes('application/json')) {
    const text = await response.text();
    if (text) {
      return JSON.parse(text);
    }
    return {};
  }

  return response;
}



export async function updateBookCreature(id, creature, setErrorMessage) {
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    setErrorMessage("Token is missing. Please log in.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(creature),
    });

    await handleError(response);

    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    } else {
      return {};
    }
  } catch (error) {
    setErrorMessage(`Unexpected error: ${error.message}`);
  }
}

export async function deleteBookCreature(id) {
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    throw new Error("Token is not available.");
  }

  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });

  await handleError(response);

  return {};
}

export async function getFilteredBookCreatures(token, filters) {
  const sanitizedFilters = Object.fromEntries(
      Object.entries(filters).filter(([_, value]) => value !== '' && value !== null)
  );

  const query = new URLSearchParams(sanitizedFilters).toString();

  const response = await fetch(`${API_BASE_URL}/find?${query}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });

  await handleError(response);

  const data = await response.json();
  return data;
}

export async function fetchImportHistory(token, userId) {
  const query = userId ? `?userId=${userId}` : "";
  const response = await fetch(`${API_BASE_URL}/history${query}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });

  await handleError(response);

  return await response.json();
}

export async function importCSVFile(file, token) {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch(`${API_BASE_URL}/import`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });

  await handleError(response);

  return await response.json();
}

function handleError(response) {
  if (!response.ok) {
    return response.json().then(errorData => {
      let errorMessage = `Error: ${response.status}`;

      if (errorData.message) {
        errorMessage = errorData.message;
      }

      let additionalMessage = '';
      if (errorData.field && errorData.errorCode) {
        additionalMessage = ` Field: ${errorData.field}, Code: ${errorData.errorCode}`;
      }

      throw new Error(errorMessage + additionalMessage);
    });
  }
  return response;
}

export async function uploadFileToMinio(file, token) {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch(`${API_BASE_URL}/minio/upload`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Ошибка загрузки файла в MinIO");
  }

  return await response.json();
}

export async function getDownloadLinkFromMinio(fileName, token) {
  const response = await fetch(`${API_BASE_URL}/minio/download-link?fileName=${encodeURIComponent(fileName)}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Ошибка получения ссылки для скачивания");
  }

  return await response.text();
}

export async function handleDownloadLink(fileName) {
  try {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      throw new Error("Авторизационный токен отсутствует.");
    }
    const downloadLink = await getDownloadLinkFromMinio(fileName, token);

    window.location.href = downloadLink;
  } catch (error) {
    console.error(error.message || "Ошибка получения ссылки для скачивания.");
  }
}



