import 'font-awesome/css/font-awesome.min.css';

const About = () => {
  return (
    <div className="settings-container">
      <img style={{ width: '250px' }} src="src/assets/logo.png" alt="" />

      <p>v1.1</p>
      <br />
      <p>Kaffee is a free and open source product.</p>
      <p>To contribute please open an issue or pull request.</p>
      <a href="https://github.com/oslabs-beta/Kaffee" target="_blank">
        <i className="fa fa-github" style={{ fontSize: '36px' }}></i>
      </a>
    </div>
  );
};

export default About;
