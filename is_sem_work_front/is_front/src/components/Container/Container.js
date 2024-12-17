import React from 'react';
import './Container.css';

const Container = (args) => {
    const finalClassName = 'container ' + (args.noMargin ? 'container--no-margin' : '') + (args.className || '')
    return (
        <div className={finalClassName}>
            {args.children}
        </div>
    );
};

export default Container;
