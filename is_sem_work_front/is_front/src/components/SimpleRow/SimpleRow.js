import React from 'react';
import './SimpleRow.css';

const SimpleRow = (args) => {
    const finalClassName = 'simple-row ' + (args.className || '')
    return (
        <div className={finalClassName}>
            {args.children}
        </div>
    );
};

export default SimpleRow;
