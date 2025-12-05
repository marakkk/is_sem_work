const fs = require('fs-extra');
const path = require('path');

function toKebabCase(string) {
  return string.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
}

function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

let blockName = process.argv[2];

if (!blockName) {
  console.error('Error: Block name is required');
  process.exit(1);
}

const dirPath = path.join(__dirname, 'src', 'components', capitalizeFirstLetter(blockName));
const kebabBlockName = toKebabCase(blockName);

fs.mkdirp(dirPath)
  .then(() => {
    console.log(`Directory ${dirPath} created`);

    const componentTemplate = `import React from 'react';
import './${capitalizeFirstLetter(blockName)}.scss';

const ${capitalizeFirstLetter(blockName)} = (args) => {
    const finalClassName = '${kebabBlockName} ' + (args.className || '')
    return (
        <div className={finalClassName}>
        </div>
    );
};

export default ${capitalizeFirstLetter(blockName)};
`;

    const scssTemplate = `.${kebabBlockName} {

    &__element {

    }
}
`;

    const componentFilePath = path.join(dirPath, `${capitalizeFirstLetter(blockName)}.js`);
    const scssFilePath = path.join(dirPath, `${capitalizeFirstLetter(blockName)}.scss`);

    fs.writeFile(componentFilePath, componentTemplate)
      .then(() => console.log(`${blockName}.js created`))
      .catch(err => console.error(`Error creating ${blockName}.js`, err));

    fs.writeFile(scssFilePath, scssTemplate)
      .then(() => console.log(`${blockName}.scss created`))
      .catch(err => console.error(`Error creating ${blockName}.scss`, err));
  })
  .catch(err => console.error(`Error creating directory ${dirPath}`, err));
