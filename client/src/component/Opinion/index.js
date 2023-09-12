import './styles.css';

import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import ReactPaginate from 'react-paginate';
import OpinionForm from './OpinionForm';
import Popup from '../Popup';
import ReactStars from 'react-rating-stars-component';
import axios from 'axios';

const Opinion=()=> {
    const path = useLocation().pathname;
    const pathSplitted = path.split("/");
    const id = pathSplitted[5];
    const [page, setPage] = useState(path.split("/").at(6) ? path.split("/").at(6) : 1);

    const [pageData, setPageData] = useState([]);
    const [specs, setSpecs] = useState({});
    const [opinions, setOpinions] = useState([]);
    const [opinionsAvg, setOpinionsAvg] = useState();

    const navigate = useNavigate();

    const [newOpinionBtn, setNewOpinionBtn] = useState(false);

    useEffect(() => {
        axios.get(`/api/car/specs?id=${id}`)
        .then((response) => setSpecs(response.data.car));

        axios.get(`/api/opinion/avg?carId=${id}`)
        .then((response) => setOpinionsAvg(response.data.rating));
    }, [id]);

    useEffect(() => {
        axios.get(`/api/opinion/list?carId=${id}&page=${page}`)
        .then((response) => setPageData(response.data));
    }, [id, page]);

    useEffect(() => {
        let opinions = pageData.opinions;
        setOpinions(opinions);
    }, [pageData.opinions]);

    const handlePageClick = (event) => {
        setPage(event.selected+1);
        navigate(`/opinie/${pathSplitted[2]}/${pathSplitted[3]}/${pathSplitted[4]}/${pathSplitted[5]}/${event.selected+1}`);
    };

    return (
        <main className="container">
            <div className='breadcrumbs'>
                <Link to='/'>MotoŚwiat</Link>
                <span> &gt; </span>
                <Link to={`/opinie`}>Opinie</Link>
                <span> &gt; </span>
                <Link to={`/opinie/${pathSplitted[2]}`}>{specs.make}</Link>
                <span> &gt; </span>
                <Link to={`/opinie/${pathSplitted[2]}/${pathSplitted[3]}`}>{specs.model}</Link>
                <span> &gt; </span>
                <Link to={`/opinie/${pathSplitted[2]}/${pathSplitted[3]}/${pathSplitted[4]}`}>{specs.generation}</Link>
                <span> &gt; {specs.trim} {specs.series}</span>
            </div>
            <h1 className='page-title'>Opinie {specs.make} {specs.model} {specs.generation} {specs.trim} {specs.series}</h1>
            <div className='content-wrapper'>
                <div className='sidebar'>
                    <div className='sidebar-element'>
                        <h4>Zobacz również</h4>
                        <div>
                            <Link to={`/dane-techniczne/${pathSplitted[2]}/${pathSplitted[3]}/${pathSplitted[4]}/${id}`}>Dane techiczne</Link>
                        </div>
                        <div>
                            <Link to={`/raporty/${pathSplitted[2]}/${pathSplitted[3]}/${pathSplitted[4]}/${id}`}>Raporty spalania</Link>
                        </div>
                        <div>
                            <span>Opinie</span>
                        </div>
                    </div>
                    <div className='sidebar-element'>
                        <div>
                            <button onClick={() => setNewOpinionBtn(true)}>Dodaj opinię</button>
                        </div>
                    </div>
                </div>
                <div className='main-container'>
                    <h2>Średnia ocena</h2>
                    <div className='rating-avg'>
                        {opinionsAvg ? opinionsAvg.toFixed(2) + "/5": "-"}
                    </div>
                    <h2>Lista opinii</h2>
                    <div>
                        {opinions && opinions.map((opinion, index) => 
                            <div className='opinion' key={index}>
                                <div className='opinion-info'>
                                    <ReactStars
                                        isHalf={true}
                                        value={opinion.rating}
                                        edit={false}
                                        size={24}
                                    />
                                    <div className='details'> {opinion.creationDate} </div>
                                </div>
                                <div className='opinion-notes'>
                                    {opinion.comment}
                                </div>
                            </div>
                        )}
                    </div>
                    <div className='pagination-container'>
                        <ReactPaginate
                            previousLabel="<"
                            breakLabel="..."
                            nextLabel=">"
                            pageCount={pageData.totalPages}
                            forcePage={page-1}
                            pageRangeDisplayed={2}
                            marginPagesDisplayed={2}
                            onPageChange={handlePageClick}
                            renderOnZeroPageCount={null}
                            pageLinkClassName="pagination-link"
                            previousLinkClassName="pagination-link"
                            nextLinkClassName="pagination-link"
                            breakLinkClassName="pagination-link"
                            containerClassName="pagination"
                        />
                    </div>
                </div>
            </div>
            <Popup trigger={newOpinionBtn} setTrigger={setNewOpinionBtn}>
                <h4 className="popup-headline">Dodaj własną opinię:</h4>
                <div className="popup-content">
                    <OpinionForm carId={id} setTrigger={setNewOpinionBtn}/>
                </div>
            </Popup>
        </main>
    )
}

export default Opinion;