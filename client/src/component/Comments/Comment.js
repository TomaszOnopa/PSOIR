import CommentForm from "./CommentForm";

const Comment=({
    comment,
    replies,
    addComment,
    activeComment,
    setActiveComment,
    parentId = null
}) => {
    const creationDate = new Date(comment.creationDate).toLocaleDateString();
    const replyId = parentId ? parentId : comment.commentId;

    const isReplying = activeComment && activeComment.type === "reply" && activeComment.id === comment.commentId;

    return(
        <div className="comment">
            <div className="image-container">
                <img src="/default-avatar.jpg" alt=""/>
            </div>
            <div className="right-part">
                <div className="content">
                    <div>{creationDate}</div>
                </div>
                <div className="text">{comment.content}</div>
                <div className="actions">
                    <div className="action" onClick={() => setActiveComment({id: comment.commentId, type: "reply"})}>Odpowiedz</div>
                </div>
                {isReplying && (
                    <CommentForm handleSubmit={(text) => addComment(text, replyId)}/>
                )}
                {replies.length > 0 && (
                    <div className="replies">
                        {replies.map(reply => (
                            <Comment
                                key={reply.commentId}
                                comment={reply}
                                replies={[]}
                                addComment={addComment}
                                activeComment={activeComment}
                                setActiveComment={setActiveComment}
                                parentId={comment.commentId}
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    )
}

export default Comment;