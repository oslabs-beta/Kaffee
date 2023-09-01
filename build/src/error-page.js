import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import { Link, useRouteError } from "react-router-dom";
import React from "react";
export default function ErrorPage() {
    const error = useRouteError();
    console.error(error);
    return (_jsxs("div", { id: "error-page", children: [_jsx("h1", { children: "Oops!" }), _jsx("p", { children: "Sorry, an unexpected error has occurred." }), _jsx("p", { children: _jsxs("i", { children: ["Page doesn't exist. Go to the ", _jsx(Link, { to: "/", children: "Home" }), " page."] }) })] }));
}
//# sourceMappingURL=error-page.js.map