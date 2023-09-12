import './styles.css';

import { Link } from "react-router-dom";

const Header=()=> {
    return (
        <header>
            <div className="container">
                <Link to='/'>
                    <img className="logo-img" src='/logo.png' alt='' />
                </Link>
            </div>
            <div className="nav-section">
                <div className="container">
                    <div className="navbar-left">
                        <Link to='/wiadomosci'>Wiadomości</Link>
                        <Link to='/dane-techniczne'>Dane techniczne</Link>
                        <Link to="/opinie">Opinie</Link>
                        <Link to="/raporty">Raporty spalania</Link>
                    </div>
                </div>
            </div>
        </header>
    );
}
export default Header