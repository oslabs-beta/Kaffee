const Feedback = () => {
  return (
    <div className="settings-container">
      <img
        data-testid="kaffee-logo"
        style={{ width: '250px' }}
        src="src/assets/logo.png"
        alt=""
      />

      <br />
      <h3>Your feedback is invaluabale! </h3>
      <h4>
        Please follow the link below to add your comments or report issues.
      </h4>
      <a
        id="link"
        href="https://github.com/oslabs-beta/Kaffee/issues"
        target="_blank"
      >
        Feedback
      </a>
    </div>
  );
};

export default Feedback;
