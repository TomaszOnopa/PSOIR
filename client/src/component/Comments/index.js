import './styles.css';

import { useEffect, useState } from 'react';
import Comment from './Comment';
import CommentForm from './CommentForm';
import axios from 'axios';

const Comments=({articleId}) => {
    const [comments, setComments] = useState([]);
    const [activeComment, setActiveComment] = useState(null);
    const rootComments = comments.filter((comment) => comment.parentId === null);
    
    function getReplies (commentId) {
        return comments
        .filter(comment => comment.parentId === commentId)
        .sort((a, b) => new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime());
    };

    function addComment (text, parentId) {
        const reqBody = {
            "articleId": articleId,
            "parentId": parentId,
            "content": text,
        }

        axios.post(`/api/comment/add`, reqBody, {
            headers: {
                "Content-Type": "application/json",
            },
        }).then((response) => {
            setComments([response.data, ...comments]);
            setActiveComment(null);
        }).catch(() => {
            alert("Wystąpił nieoczekiwany błąd");
        });
    };
    
    useEffect(() => {
        axios.get(`/api/comment?articleId=${articleId}`)
        .then((response) => setComments(response.data.comments));
    },[articleId])

    return(
        <div className='comments'>
            <h2>Lista komentarzy</h2>
            <>
            <div className='comment-form-title'>Napisz komentarz:</div>
            <CommentForm handleSubmit={addComment}/>
            </>
            <div className='comments-container'>
                {rootComments.map(rootComment => (
                    <Comment
                        key={rootComment.commentId}
                        comment={rootComment}
                        replies={getReplies(rootComment.commentId)}
                        addComment={addComment}
                        activeComment={activeComment}
                        setActiveComment={setActiveComment}
                    />
                ))}
            </div>
        </div>
    )
}

export default Comments;