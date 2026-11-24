import { useEffect } from "react";

const TitleWrapper = ({ title, children }: any) => {
    useEffect(() => {
        if (title) document.title = title;
    }, [title]);

    return children;
};

export default TitleWrapper;
