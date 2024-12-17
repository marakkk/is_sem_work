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



