import React, { useState } from 'react';
import { AuthProvider } from './contexts/AuthContext';
import { CartProvider } from './contexts/CartContext';
import { Header } from './components/Header';
import { HomePage } from './pages/HomePage';
import { ProductsPage } from './pages/ProductsPage';
import { ProductDetailPage } from './pages/ProductDetailPage';
import { CartPage } from './pages/CartPage';
import { CheckoutPage } from './pages/CheckoutPage';
import { AuthPage } from './pages/AuthPage';
import { SellerDashboard } from './pages/SellerDashboard';
import { OrderSuccessPage } from './pages/OrderSuccessPage';
import { useAuth } from './contexts/AuthContext';

function AppContent() {
  const { user } = useAuth();
  const [currentPage, setCurrentPage] = useState('home');
  const [selectedProductId, setSelectedProductId] = useState<string | null>(null);

  const handlePageChange = (page: string) => {
    setCurrentPage(page);
    if (page !== 'product-detail') {
      setSelectedProductId(null);
    }
  };

  const handleProductClick = (productId: string) => {
    setSelectedProductId(productId);
    setCurrentPage('product-detail');
  };

  const handleAuthSuccess = () => {
    setCurrentPage('home');
  };

  const handleOrderComplete = () => {
    setCurrentPage('order-success');
  };

  // Render current page
  const renderCurrentPage = () => {
    switch (currentPage) {
      case 'home':
        return <HomePage onPageChange={handlePageChange} onProductClick={handleProductClick} />;
      case 'products':
        return <ProductsPage onProductClick={handleProductClick} />;
      case 'product-detail':
        return selectedProductId ? (
          <ProductDetailPage
            productId={selectedProductId}
            onBack={() => handlePageChange('products')}
          />
        ) : (
          <ProductsPage onProductClick={handleProductClick} />
        );
      case 'cart':
        return <CartPage onPageChange={handlePageChange} />;
      case 'checkout':
        return user ? (
          <CheckoutPage onOrderComplete={handleOrderComplete} />
        ) : (
          <AuthPage onSuccess={handleAuthSuccess} />
        );
      case 'auth':
        return <AuthPage onSuccess={handleAuthSuccess} />;
      case 'seller-dashboard':
        return user?.type === 'seller' ? (
          <SellerDashboard onPageChange={handlePageChange} />
        ) : (
          <HomePage onPageChange={handlePageChange} onProductClick={handleProductClick} />
        );
      case 'order-success':
        return <OrderSuccessPage onPageChange={handlePageChange} />;
      case 'about':
        return (
          <div className="min-h-screen bg-gray-50 flex items-center justify-center">
            <div className="text-center">
              <h1 className="text-4xl font-bold text-gray-900 mb-4">About ShopHub</h1>
              <p className="text-xl text-gray-600 max-w-2xl">
                ShopHub is your premier destination for discovering amazing products from sellers worldwide.
                We connect buyers and sellers in a seamless, secure marketplace experience.
              </p>
            </div>
          </div>
        );
      default:
        return <HomePage onPageChange={handlePageChange} onProductClick={handleProductClick} />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header onPageChange={handlePageChange} currentPage={currentPage} />
      <main>
        {renderCurrentPage()}
      </main>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <AppContent />
      </CartProvider>
    </AuthProvider>
  );
}

export default App;