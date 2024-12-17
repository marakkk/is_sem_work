import React from 'react';
import './Content.css';
import Container from '../Container/Container';

const Content = (args) => {
    const finalClassName = 'content ' + (args.className || '')
    return (
        <div className={finalClassName} style={{ backgroundImage: `url('${args.background}')` }}>
            <Container noMargin={args.noMargin}>
                {args.children}
            </Container>
        </div>
    );
};

export default Content;
